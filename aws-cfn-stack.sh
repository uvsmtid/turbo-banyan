#!/usr/bin/env bash

# Wrapper script to create/update/delete CI/CD pipeline via AWS CloudFormation.
# Usage:
# ./aws-cfn-stack.sh [ validate | create | update | cancel | delete ]

# Intentionally set excessive debug output and error detection:
# *   Fail (stop) script on non-zero exit code from any command.
set -e
# *   Use error code of the first failed command within a pipe.
set -o pipefail
# *   Fail on detection of any undefined variable.
set -u
# *   Print trace.
set -x
# *   Print each command before execution.
set -v

sub_command="${1}"

# Set up a trap on exit:
function exit_trap {
    exit_code="${?}"
    if [[ "${exit_code}" == "0" ]]
    then
        echo "SUCCESS: exit_code=$exit_code: everything seems fine" 2>&1
    else
        echo "FAILURE: exit_code=$exit_code: check output above for details" 2>&1
    fi
}
trap exit_trap EXIT

# Source configuration:
source ./aws-cfn-stack.conf

# AWS CLI must be:
# *   Installed: https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-linux.html
# *   Configured: https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-quickstart.html
aws --version

case "${sub_command}" in
    validate)
        aws cloudformation \
            validate-template \
            --region "${AWS_REGION}" \
            --template-body file://aws-cfn-stack.yaml \

    ;;
    create)
        aws cloudformation \
            create-stack \
            --region "${AWS_REGION}" \
            --stack-name "${AWS_STACK_NAME}" \
            --template-body file://aws-cfn-stack.yaml \
            --capabilities CAPABILITY_NAMED_IAM \
            --parameters \
                ParameterKey=ParAwsAccoundId,ParameterValue="${AWS_ACCOUNT}" \
                ParameterKey=ParGitHubAccountName,ParameterValue="${GIT_HUB_ACCOUNT}" \
                ParameterKey=ParGitHubTokenSecretName,ParameterValue="${GIT_HUB_API_TOKEN_SECRET_NAME}" \
                ParameterKey=ParBranchName,ParameterValue="${BRANCH_NAME}" \

    ;;
    update)
        aws cloudformation \
            update-stack \
            --region "${AWS_REGION}" \
            --stack-name "${AWS_STACK_NAME}" \
            --template-body file://aws-cfn-stack.yaml \
            --capabilities CAPABILITY_NAMED_IAM \
            --parameters \
                ParameterKey=ParAwsAccoundId,ParameterValue="${AWS_ACCOUNT}" \
                ParameterKey=ParGitHubAccountName,ParameterValue="${GIT_HUB_ACCOUNT}" \
                ParameterKey=ParGitHubTokenSecretName,ParameterValue="${GIT_HUB_API_TOKEN_SECRET_NAME}" \
                ParameterKey=ParBranchName,ParameterValue="${BRANCH_NAME}" \

    ;;
    cancel)
        aws cloudformation \
            cancel-update-stack \
            --region "${AWS_REGION}" \
            --stack-name "${AWS_STACK_NAME}" \

    ;;
    delete)
        aws cloudformation \
            delete-stack \
            --region "${AWS_REGION}" \
            --stack-name "${AWS_STACK_NAME}" \

    ;;
    *)
        echo "ERROR: unknown sub command: ${sub_command}" >&2
        exit 1
    ;;
esac

