terraform {
  source = "./rds-module"
}

include "root" {
  path = find_in_parent_folders()
}

dependency "vpc" {
  config_path = "../vpc"
  mock_outputs = {
    vpc_id                     = "vpc-05a4dcdb2152594d4"
    private_subnets            = ["subnet-0f20bec0faadca5c2", "subnet-0adb240ce4e56a562"]
    vpc_cidr_block             = "10.0.0.0/16"
    database_subnet_group_name = "compass-database-subnet"
  }
}

locals {
  common_vars = read_terragrunt_config("../../common.hcl")
}

inputs = {
  vpc_id               = dependency.vpc.outputs.vpc_id
  vpc_cidr_block       = "${local.common_vars.locals.vpc_cidr}"
  private_subnets      = dependency.vpc.outputs.private_subnets
  db_subnet_group_name = dependency.vpc.outputs.database_subnet_group_name
}