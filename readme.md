
## What is this? ##

This is a demo service with:
*   REST interface
*   MySQL backend database
*   AWS CloudFormation to provision its CI/CD pipeline

There are three sections below (`A`, `B`, `C`) from trivial to complex:
*   **`A`**: [local build and run (Maven)](#A)
*   **`B`**: [local build and run (Docker)](#B)
*   **`C`**: [spawning cloud CI/CD pipeline (AWS CloudFormation)](#C)

You may want to jump directly to `C`.

Depending on the section - Git, Java, Maven, Docker, AWS CLI, ... are assumed functional on the local machine.

<a name="config"></a>

## Configuration before the start ##

Ultimately, the demo is about CI/CD pipeline in AWS integrated with github.com.

The following steps explain and prepare parameters for `aws-cnf-stack.conf` configuration file.

*   Specify your AWS account and please stick with specific AWS region.

    ```
    AWS_ACCOUNT="243535590163"
    ```

    Obviously, the IAM user under this account
    (for `aws` CLI configuration) needs necessary permissions.

    This demo was only tested in the following region
    (preferred as `aws-cfn-stack.yaml` template may lack additional `Mappings`):

    ```sh
    AWS_REGION="ap-southeast-1"
    ```

*   Use your own github.com account to be watched by the pipeline.

    Make a *fork* of this repository:

    https://github.com/uvsmtid/turbo-banyan

    Your github.com account will subsequently be referenced via
    this environment variable (replace `uvsmtid`):

    ```sh
    GIT_HUB_ACCOUNT="uvsmtid"
    ```

*   Generate new API token on github.com and store it in AWS Secrets Manager.

    The name of the secret will be stored in this environment variable:

    ```sh
    GIT_HUB_API_TOKEN_SECRET_NAME="turbo-banyan-github-token"
    ```

    NOTE:
    https://docs.aws.amazon.com/codepipeline/latest/userguide/update-change-detection.html
    > The token and webhook require the following GitHub scopes:
    > * `repo` - used for full control to read and pull artifacts from public and private repositories into a pipeline.
    > * `admin:repo_hook` - used for full control of repository hooks.

    Create the token: https://github.com/settings/tokens

    Store token value into AWS secret (replace `0000000000000000000000000000000000000000`):

    ```sh
    # NOTE: this command has issues of being extremely slow to execute (up to few minutes).
    aws \
        secretsmanager \
        create-secret \
        --region "${AWS_REGION}" \
        --name "${GIT_HUB_API_TOKEN_SECRET_NAME}" \
        --secret-string '{"token":"0000000000000000000000000000000000000000"}'
    ```

<a name="A"></a>

## **`A`**: Start via sources ##

If started via sources, the service uses embedded in-memory database.

*   Start the service (in one terminal):

    ```sh
    mvn clean test && mvn exec:java \
        -Dspring.profiles.active=local-dev \
        --projects=turbo-banyan-student-service
    ```

*   Test the service (in another terminal):

    ```sh
    root_url="http://localhost:8080/"
    ./run-test-requests.sh "${root_url}"
    ```

<a name="B"></a>

## **`B`**: Start via images ##

If started via images, the service requires available MySQL instance with initialised database.

> TODO: Consider managing both Docker instances via `docker-compose`.

*   Start a database instance:

    ```sh
    docker run \
        --name mysql-instance \
        --rm \
        -p 3306:3306 \
        -p 33060:33060 \
        -e MYSQL_ROOT_HOST='%' \
        -e MYSQL_ROOT_PASSWORD=root_turbo_banyan_password \
        -d mysql:latest
    ```

    Please wait - MySQL startup normally takes around 30 sec.

*   Init the database:

    <!--
    NOTE: interactive login:

    ```sh
    docker exec -it mysql-instance mysql -hlocalhost -uroot -ppassword
    ```
    -->

    ```sh
    docker exec \
        mysql-instance \
        mysql \
        -hlocalhost \
        -uroot \
        -proot_turbo_banyan_password \
        -e "$(cat turbo-banyan-student-service/src/main/resources/database/init-database.sql)"
    ```

*   Build a service image:

    ```sh
    mvn package
    ```

*   Run a service image:

    ```
    docker run \
        --rm \
        -p 8080:8080 \
        -e SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/turbo_banyan_database' \
        -e SPRING_DATASOURCE_USERNAME='turbo_banyan_username' \
        -e SPRING_DATASOURCE_PASSWORD='turbo_banyan_password' \
        --network=host \
        uvsmtid/turbo-banyan-student-service:latest
    ```

*   Test the service:

    ```sh
    root_url="http://localhost:8080/"
    ./run-test-requests.sh "${root_url}"
    ```

<a name="C"></a>

## **`C`**: Start via CI/CD ##

Check [configuration before the start](#config).

*   Create the pipeline:

    > TODO: Consider modifying script to merge `create` and `update` operations into one (e.g. `push`)

    ```sh
    ./aws-cfn-stack.sh create
    ```

    Monitor provisioning of the pipeline - it may take several minutes:

    https://console.aws.amazon.com/cloudformation

    If changes are necessary, use `update` command subsequently:

    ```sh
    ./aws-cfn-stack.sh update
    ```

*   Push a commit (e.g. modify `readme.md`) to trigger the pipeline:

    ```sh
    echo "" >> readme.md && \
    git add . && \
    git commit -m "Add empty new line to readme.md" && \
    git push origin
    ```

    Monitor pipeline execution - it may take several minutes:

    https://console.aws.amazon.com/codesuite/codepipeline/pipelines

*   Test the service:

    Find DNS name of the relevant balancer in `DNSName` field:

    ```sh
    aws elbv2 \
        describe-load-balancers \
        --region "${AWS_REGION}"
    ```

    Use the DNS name as hostname for the `root_url`:

    ```sh
    root_url="http://${load_balancer_dns_name}/"
    ./run-test-requests.sh "${root_url}"
    ```

## Tear down CI/CD ##

*   Delete the pipeline:

    ```sh
    ./aws-cfn-stack.sh delete
    ```

## References ##

*   The AWS Journey Part 2: Deploying a Docker Image with AWS CloudFormation

    https://reflectoring.io/aws-cloudformation-deploy-docker-image/

*   The AWS Journey Part 3: Connecting a Spring Boot Application to an RDS Instance with CloudFormation

    https://reflectoring.io/aws-cloudformation-rds/

*   Delivery Pipeline as Code: AWS CloudFormation and AWS CodePipeline

    https://cloudonaut.io/delivery-pipeline-as-code-aws-cloudformation-codepipeline/

---

