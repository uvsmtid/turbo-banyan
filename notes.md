This file contains various useful notes.

*   Installing the AWS CLI version 2 on Linux

    https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-linux.html

    TLDR:

    ```sh
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
    unzip awscliv2.zip
    sudo ./aws/install
    aws --version
    ```

    Install without `sudo`:

    ```sh
    # Some directory for stuff like that:
    mkdir -p ~/Apps/aws
    cd ~/Apps/aws
    curl "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" -o "awscli-bundle.zip"
    unzip awscli-bundle.zip
    ./awscli-bundle/install -b ~/bin/aws
    ```

*   Configuring the AWS CLI

    https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-quickstart.html

    TLDR:

    Create access keys:

    *   https://console.aws.amazon.com/iam/
    *   Users
    *   Security credentials
    *   Create access key
    *   Copy: Access key ID
    *   Copy: Secret access key

    ```sh
    aws configure
    # ...
    ls ~/.aws/*
    ```

    Default region: `ap-southeast-1`

    By default, the information in `default` profile from `credentials` file is used when you run an AWS CLI command that doesn't explicitly specify a profile to use.

*   Tutorial: Create a simple pipeline (CodeCommit repository)

    https://docs.aws.amazon.com/codepipeline/latest/userguide/tutorials-simple-codecommit.html

    TODO: Translate all steps into CLI.

*   Start a pipeline manually in AWS CodePipeline

    https://docs.aws.amazon.com/codepipeline/latest/userguide/pipelines-rerun-manually.html

    TLDR:

    ```sh
    aws codepipeline start-pipeline-execution --name turbo-banyan-pipeline
    ```

*   Create a build project (AWS CLI)

    https://docs.aws.amazon.com/codebuild/latest/userguide/create-project-cli.html

    TLDR:

    ```sh
    aws codebuild create-project --cli-input-yaml file://create-project.yaml
    ```

*   CodeDeploy sample for CodeBuild

    https://docs.aws.amazon.com/codebuild/latest/userguide/sample-codedeploy.html

    TLDR: See `buildspec.yaml` and `appspec.yaml`.

*   Docker sample for CodeBuild

    https://docs.aws.amazon.com/codebuild/latest/userguide/sample-docker.html

    *   Adapting the sample to push the image to Docker Hub

        https://docs.aws.amazon.com/codebuild/latest/userguide/sample-docker.html#sample-docker-docker-hub

        TODO

*   Docker in custom image sample for CodeBuild

    https://docs.aws.amazon.com/codebuild/latest/userguide/sample-docker-custom-image.html

*   Ready to use AWS CloudFormation:

    *   Blue-Green Deployment on AWS: https://aws.amazon.com/quickstart/architecture/blue-green-deployment/
    *   CI/CD Pipeline for AWS CloudFormation templates on AWS: https://aws.amazon.com/quickstart/architecture/cicd-taskcat/

*   Deploying Java Microservices on Amazon Elastic Container Service

    https://aws.amazon.com/blogs/compute/deploying-java-microservices-on-amazon-ec2-container-service/

    *   Existing sample using AWS CloudFormation.
    *   Uses Docker.

*   Another AWD CloudFormation example:

    https://github.com/codeurjc/spring-cloud-aws-sample#using-cloudformation

*   Create/Update/Delete stack CLI:

    NOTE: https://docs.aws.amazon.com/AWSCloudFormation/latest/APIReference/API_CreateStack.html
    > If you have IAM resources with custom names, you must specify `CAPABILITY_NAMED_IAM`.

    ```sh
    aws cloudformation create-stack \
        --stack-name turbo-banyan-stack \
        --template-body file://pipeline.cloudformation.template.yaml \
        --capabilities CAPABILITY_NAMED_IAM \

    ```

    ```sh
    aws cloudformation update-stack \
        --stack-name turbo-banyan-stack \
        --template-body file://pipeline.cloudformation.template.yaml \
        --capabilities CAPABILITY_NAMED_IAM \

    ```

    ```sh
    aws cloudformation delete-stack \
        --stack-name turbo-banyan-stack \

    ```

