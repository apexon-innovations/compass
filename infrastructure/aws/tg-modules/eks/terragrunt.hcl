terraform {
  source = "./eks-module"
}

include "root" {
  path = find_in_parent_folders()
}

dependency "vpc" {
  config_path = "../vpc"
  mock_outputs = {
    vpc_id           = "vpc-05a4dcdb2152594d4"
    private_subnet_1 = "subnet-0f20bec0faadca5c2"
    private_subnet_2 = "subnet-0adb240ce4e56a562"
    private_subnet_3 = "subnet-0adb240ce4e56a562"
  }
}

locals {
  common_vars = read_terragrunt_config("../../common.hcl")
}

inputs = {
  cluster_name     = "compass-${local.common_vars.locals.env}-cluster"
  cluster_version  = "${local.common_vars.locals.cluster_version}"
  vpc_id           = dependency.vpc.outputs.vpc_id
  private_subnet_1 = dependency.vpc.outputs.private_subnet_1
  private_subnet_2 = dependency.vpc.outputs.private_subnet_2
  private_subnet_3 = dependency.vpc.outputs.private_subnet_3

  ami_release_version = "1.29.0-20240129"

}