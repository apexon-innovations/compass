terraform {
  source = "./ec2-mongodb"
}

include "root" {
  path = find_in_parent_folders()
}

dependency "vpc" {
  config_path = "../vpc"
  mock_outputs = {
    vpc_id            = "vpc-05a4dcdb2152594d4"
    private_subnet_id = "subnet-0f20bec0faadca5c2"
    vpc_cidr          = "10.0.0.0/16"
    instance_type     = "t2.medium"
  }
}

locals {
  common_vars = read_terragrunt_config("../../common.hcl")
}

inputs = {
  vpc_id            = dependency.vpc.outputs.vpc_id
  vpc_cidr          = "${local.common_vars.locals.vpc_cidr}"
  private_subnet_id = dependency.vpc.outputs.private_subnets[0]
  instance_type     = "t2.medium"
}