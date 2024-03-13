variable "vpc_cidr" {
  description = "provide vpc cidr"
}

variable "cluster_name" {
  description = "cluster name"
}

variable "environment" {
  description = "environment prod"
  default     = "prod"
}