
## What is this? ##

This is a demo service with:
*   REST interface
*   MySQL backend database
*   AWS CloudFormation to provision its CI/CD pipeline

## Before the start ##

Ultimately, the demo is about CI/CD pipeline in AWS integrated with github.com.

*   Please stick with specific AWS region.

    This demo was only tested in the following region
    (preferred as template may lack the necessary mappings):

    ```sh
    AWS_REGION=ap-southeast-1
    ```

    Obviously, use the IAM user with necessary permissions.

*   Please create your own github.com account to be watched by the pipeline.

    Make *fork* of this repository first:

    https://github.com/uvsmtid/turbo-banyan

    Your github.com account will subsequently be referenced via
    this environment variable:

    ```sh
    GIT_HUB_ACCOUNT="uvsmtid"
    ```

## Various service start methods ##

There are three sections below (`A`, `B`, `C`) from trivial to complex.

You may want to jump directly to `C` with the CI/CD pipeline in AWS.

Depending on the section - Git, Java, Maven, Docker, AWS CLI, ... are assumed functional on the local machine.

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

## **`B`**: Start via images ##

If started via images, the service requires available MySQL instance with initialised database.

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

## **`C`**: Start via CI/CD ##

*   Create the pipeline:

    TODO: modify script to make create/update indistinguishable (call it `push`).

    ```sh
    ./aws-cfn-stack.sh create
    ```

    Monitory provisioning of the pipeline - it may take several minutes:

    https://console.aws.amazon.com/cloudformation

    If changes are necessary, use `update` command subsequently:

    ```sh
    ./aws-cfn-stack.sh update
    ```

*   Push a commit (e.g. modify `readme.md`) to trigger the pipeline:

    ```sh
    echo "" >> readme.md
    git add .
    git commit -m "Add empty new line to readme.md"
    git push origin
    ```

    Monitor pipeline execution - it may take several minutes:

    https://console.aws.amazon.com/codesuite/codepipeline/pipelines

*   Test the service:

    Find DNS name of the relevant balancer in `DNSName` field:

    ```sh
    aws elbv2 describe-load-balancers
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

---

