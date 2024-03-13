output "alb_arn" {
  value = module.alb.lb_arn
}

output "public_dns" {
  value = module.alb.lb_dns_name
}