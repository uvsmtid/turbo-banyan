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

*   Start a pipeline manually in AWS CodePipeline

    https://docs.aws.amazon.com/codepipeline/latest/userguide/pipelines-rerun-manually.html

    TLDR:

    ```sh
    aws codepipeline start-pipeline-execution --name turbo-banyan-pipeline
    ```

*   CodeDeploy sample for CodeBuild

    https://docs.aws.amazon.com/codebuild/latest/userguide/sample-codedeploy.html

    TLDR: See `buildspec.yaml` and `appspec.yaml`.

*   Ready to use AWS CloudFormation:

    *   Blue-Green Deployment on AWS: https://aws.amazon.com/quickstart/architecture/blue-green-deployment/
    *   CI/CD Pipeline for AWS CloudFormation templates on AWS: https://aws.amazon.com/quickstart/architecture/cicd-taskcat/
