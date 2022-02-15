output "public_id" {
  value = aws_instance.ssh.public_ip
  description = "Public IP"
}