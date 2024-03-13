terraform {
  extra_arguments "common_vars" {
    commands = get_terraform_commands_that_need_vars()
  }

  extra_arguments "disable_input" {
    commands  = get_terraform_commands_that_need_input()
    arguments = ["-input=false"]
  }
}

generate "main_providers" {
  path      = "main_providers.tf"
  if_exists = "overwrite"
  contents  = <<EOF
provider "aws" {
  region = var.aws_region

  # Make it faster by skipping something
  skip_metadata_api_check     = true
  skip_region_validation      = true
  skip_credentials_validation = true
}

variable "aws_region" {
  description = "AWS region to create infrastructure in"
  type        = string
}

variable "common_parameters" {
  description = "Map of common parameters shared across all infrastructure resources (eg, domain names)"
  type        = map(string)
  default     = {}
}
EOF
}

remote_state {
  backend = "s3"
  generate = {
    path      = "_backend.tf"
    if_exists = "overwrite_terragrunt"
  }
  config = {
    key            = format("%s/terraform.tfstate", path_relative_to_include())
    region         = "us-east-1"
    encrypt        = true
    bucket         = format("compass-terraform-states-%s", get_aws_account_id())
    dynamodb_table = format("compass-terraform-states-%s", get_aws_account_id())
  }
}
