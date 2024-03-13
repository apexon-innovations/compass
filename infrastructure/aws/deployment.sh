#/bin/sh

echo "Deploying Compass infra components"
# Function to check and exit on error
check_error() {
    local exit_code=$1
    local error_message=$2

    if [ $exit_code -ne 0 ]; then
        echo "Error: $error_message"
        exit $exit_code
    fi
}

account_id=$(aws sts get-caller-identity --query Account | tr -d '"')
check_error $? "No AWS Creds or Role found"

# Terraform initialization
echo "Initializing Terragrunt..."
yes | terragrunt run-all init
check_error $? "Terragrunt initialization failed."

# Apply Terraform changes
echo "Applying Terragrunt changes..."
echo "Y" | terragrunt apply-all
check_error $? "Terragrunt apply failed."
