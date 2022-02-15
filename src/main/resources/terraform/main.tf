resource "aws_instance" "ssh" {
  ami = "ami-08edbb0e85d6a0a07"
  instance_type = "t2.micro"
  vpc_security_group_ids = [aws_security_group.ssh.id]
  associate_public_ip_address = true

  tags = {
    Name = "terraform-ssh"
  }

  key_name = "ssh-key"
}