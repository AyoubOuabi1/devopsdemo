# Downloading and Installing Jenkins on AWS EC2

This guide provides step-by-step instructions for downloading and installing Jenkins on an Amazon EC2 instance with Amazon Linux. Before you begin, ensure you have an EC2 instance running Amazon Linux and connect to the instance.

## Prerequisites

- An Amazon EC2 instance with Amazon Linux.
- Access to the EC2 instance through SSH.

## Introduction

Completing the following steps will enable you to download and install Jenkins on your AWS EC2 instance. Follow the steps below after connecting to your EC2 instance.

## Step 1: Update Software Packages

Ensure that your software packages are up to date on your instance by using the following command to perform a quick software update:

```bash
[ec2-user ~]$ sudo yum update –y
```
## Step 2: Add Jenkins Repository

Add the Jenkins repository using the following command:

```bash
[ec2-user ~]$ sudo wget -O /etc/yum.repos.d/jenkins.repo \ https://pkg.jenkins.io/redhat-stable/jenkins.repo
```

## Step 3: Import Jenkins-CI Key

Import a key file from Jenkins-CI to enable installation from the package:

```bash
[ec2-user ~]$ sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io-2023.key
[ec2-user ~]$ sudo yum upgrade
```

## Step 4: Install Java

Install Java :

```bash
[ec2-user ~]$ sudo dnf install java-17-amazon-corretto -y
```
## Step 5: Install Jenkins

Install Jenkins:

```bash
[ec2-user ~]$ sudo yum install jenkins -y
```

## Step 6: Enable Jenkins Service

Enable the Jenkins service to start at boot:

```bash
[ec2-user ~]$ sudo systemctl enable jenkins
```

## Step 7: Start Jenkins Service

Start Jenkins as a service:

```bash
[ec2-user ~]$ sudo systemctl start jenkins
```
## Step 8: Check Jenkins Service Status

You can check the status of the Jenkins service using the command: 

```bash
[ec2-user ~]$ sudo systemctl status jenkins
```

## Step 9: Access Jenkins Management Interface


Connect to http://<your_server_public_DNS>:8080 from your browser.

```bash

Replace `<your_server_public_DNS>` with the actual public DNS of your EC2 instance.
```

## Step 10 : Retrieve Initial Admin Password
As prompted, enter the password found in /var/lib/jenkins/secrets/initialAdminPassword. Use the following command to display this password:

```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```
