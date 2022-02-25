resource "aws_key_pair" "ssh-key" {
  key_name   = "ssh-key-id"
  public_key = "pkey"
}