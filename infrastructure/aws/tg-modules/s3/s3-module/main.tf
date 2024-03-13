data "aws_caller_identity" "current" {}

resource "aws_s3_bucket" "aws_s3_bucket_client" {
  bucket        = "compass-microfrontend-${var.environment}-${data.aws_caller_identity.current.account_id}-client"
  force_destroy = true
}

resource "aws_s3_bucket" "aws_s3_bucket_login" {
  bucket        = "compass-microfrontend-${var.environment}-${data.aws_caller_identity.current.account_id}-login"
  force_destroy = true
}

resource "aws_s3_bucket" "aws_s3_bucket_pmo" {
  bucket        = "compass-microfrontend-${var.environment}-${data.aws_caller_identity.current.account_id}-pmo"
  force_destroy = true
}

resource "aws_s3_bucket" "aws_s3_bucket_logo" {
  bucket        = "compass-microfrontend-${var.environment}-${data.aws_caller_identity.current.account_id}-logo"
  force_destroy = true
}


resource "aws_cloudfront_distribution" "compass_distribution" {
  origin {
    domain_name = aws_s3_bucket.aws_s3_bucket_client.bucket_regional_domain_name
    origin_id   = "client"
    s3_origin_config {
      origin_access_identity = aws_cloudfront_origin_access_identity.access_identity_assets.cloudfront_access_identity_path
    }
  }

  origin {
    domain_name = aws_s3_bucket.aws_s3_bucket_pmo.bucket_regional_domain_name
    origin_id   = "pdo"
    s3_origin_config {
      origin_access_identity = aws_cloudfront_origin_access_identity.access_identity_assets.cloudfront_access_identity_path
    }
  }

  origin {
    domain_name = aws_s3_bucket.aws_s3_bucket_login.bucket_regional_domain_name
    origin_id   = "pmo"
    s3_origin_config {
      origin_access_identity = aws_cloudfront_origin_access_identity.access_identity_assets.cloudfront_access_identity_path
    }
  }

  enabled             = true
  is_ipv6_enabled     = true
  comment             = "Cloudfront distribution for the compass frontend"
  default_root_object = "index.html"


  ordered_cache_behavior {
    path_pattern     = "/pdo/*"
    target_origin_id = "pdo"
    allowed_methods  = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
    cached_methods   = ["GET", "HEAD"]
    #cache_policy_id  = aws_cloudfront_cache_policy.cloudfront_cache_policy.id
    #origin_request_policy_id = aws_cloudfront_origin_request_policy.cloudfront_request_policy.id
    viewer_protocol_policy = "redirect-to-https"
    forwarded_values {
      query_string = true
      cookies {
        forward = "all"
      }
      headers = ["All"]
    }
    min_ttl     = 0
    default_ttl = 3600
    max_ttl     = 86400
  }

  ordered_cache_behavior {
    path_pattern     = "/pmo/*"
    target_origin_id = "pmo"
    allowed_methods  = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
    cached_methods   = ["GET", "HEAD"]
    #cache_policy_id          = aws_cloudfront_cache_policy.cloudfront_cache_policy.id
    #origin_request_policy_id = aws_cloudfront_origin_request_policy.cloudfront_request_policy.id
    viewer_protocol_policy = "redirect-to-https"
    forwarded_values {
      query_string = true
      cookies {
        forward = "all"
      }
      headers = ["All"]
    }
    min_ttl     = 0
    default_ttl = 3600
    max_ttl     = 86400
  }

  default_cache_behavior {
    allowed_methods  = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "client"
    forwarded_values {
      query_string = true
      cookies {
        forward = "all"
      }
      headers = ["All"]
    }
    min_ttl     = 0
    default_ttl = 3600
    max_ttl     = 86400
    #cache_policy_id          = aws_cloudfront_cache_policy.cloudfront_cache_policy.id
    #origin_request_policy_id = aws_cloudfront_origin_request_policy.cloudfront_request_policy.id
    viewer_protocol_policy = "redirect-to-https"
  }




  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }


  custom_error_response {
    error_code            = 404
    response_code         = 200
    response_page_path    = "/index.html"
    error_caching_min_ttl = 10
  }
}

resource "aws_cloudfront_cache_policy" "cloudfront_cache_policy" {
  name        = "compass-cloudfront-cache-policy"
  comment     = "Cache policy for avm"
  default_ttl = 0
  max_ttl     = 3600
  min_ttl     = 0
  parameters_in_cache_key_and_forwarded_to_origin {
    headers_config {
      header_behavior = "allViwer"
    }
    query_strings_config {
      query_string_behavior = "all"
    }
    cookies_config {
      cookie_behavior = "all"
    }
  }
}

resource "aws_cloudfront_origin_request_policy" "cloudfront_request_policy" {
  name    = "compass-cloudfront-origin-request-policy"
  comment = "request policy for website factor"
  cookies_config {
    cookie_behavior = "all"
  }
  headers_config {
    header_behavior = "none"
  }
  query_strings_config {
    query_string_behavior = "all"
  }
}

resource "aws_cloudfront_origin_access_identity" "access_identity_assets" {
  comment = "Assets access identity"
}
