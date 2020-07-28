#!/usr/bin/env bash

# This script runs few test requests as a client to the service.
# NOTE: It is meant to be a smoke test from basic environments with `bash` and `curl`.
#       Use normal language for anything more than that.

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

service_base_url="${1}"

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

# Perform HTTP request, ensure status code and print response on STDERR:
function ensure_response_on_request {

    # GIVEN

    output_file="$(mktemp)"
    expected_status_code="${1}"
    request_type="${2}"
    request_url="${3}"
    request_body="${4}"

    # WHEN

    status_code="$(\
        curl \
            --output "${output_file}" \
            --silent "${request_url}" \
            --request "${request_type}"  \
            --header "Content-Type: application/json" \
            --header "Accept: application/json" \
            --write-out "%{http_code}" \
            --data "${request_body}" \
    )"

    # THEN

    echo "status_code: ${status_code}"

    echo "response_body: "
    cat "${output_file}"
    echo

    test "${status_code}" == "${expected_status_code}"
}

#######################################
# get by unknown id

ensure_response_on_request 404 GET "${service_base_url}students/12345" \
'
{
    "firstName": "Muhammadu",
    "lastName": "Buhari",
    "class": "88",
    "nationality": "Nigeria"
}
'

#######################################
# post new student

ensure_response_on_request 200 POST "${service_base_url}students/" \
'
{
    "firstName": "Muhammadu",
    "lastName": "Buhari",
    "class": "88",
    "nationality": "Nigeria"
}
'

ensure_response_on_request 200 POST "${service_base_url}students/" \
'
{
    "firstName": "Lotay",
    "lastName": "Tshering",
    "class": "88",
    "nationality": "Bhutan"
}
'

#######################################
# get all students

ensure_response_on_request 200 GET "${service_base_url}students/" \
'
{
    "firstName": "Muhammadu",
    "lastName": "Buhari",
    "class": "88",
    "nationality": "Nigeria"
}
'
