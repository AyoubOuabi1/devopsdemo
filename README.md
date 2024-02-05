# Downloading and Installing Jenkins and SonarQube on AWS EC2

This guide provides step-by-step instructions for downloading and installing Jenkins and SonarQube on an Amazon EC2 instance. Before you begin, ensure you have an EC2 instance running Amazon Linux and connect to the instance.

## Prerequisites

- An Amazon EC2 instance with Amazon Linux(for Jenkins).
- An Amazon EC2 instance with Ubunto(for SonarQube).
- Access to the EC2 instance through SSH.

## Introduction

Completing the following steps will enable you to download and install Jenkins on your AWS EC2 instance. Follow the steps below after connecting to your EC2 instance.

## Start Intalling Jenkins

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
![unlock_jenkins](https://github.com/AyoubOuabi1/devopsdemo/assets/112890204/17d50109-358f-49b8-b6ca-3229fd754f2d)

## Step 10 : Retrieve Initial Admin Password
As prompted, enter the password found in /var/lib/jenkins/secrets/initialAdminPassword. Use the following command to display this password:

```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```
## Step 10 : Install Git 

Install git 

```bash
sudo yum install git
```

## Step 11 : To add Git to Jenkins:

Open the Jenkins dashboard in your web browser by navigating to http://<your_server_public_DNS>:8080.

Log in to Jenkins using your credentials.

Click on "Manage Jenkins" in the Jenkins dashboard.

Select "Global Tool Configuration."

Scroll down to the "Git" section.

Click on "Add Git" to add a new Git installation.

Provide a name for the Git installation (e.g., "Default" or "Git").

Specify the path to the Git executable. This is typically located at ```bash  /usr/bin/git ``` on Amazon Linux.

![image](https://github.com/AyoubOuabi1/devopsdemo/assets/112890204/7156a2f2-3cf6-4710-95ff-7bab410b6041)

Click "Save" to save the configuration.

## Step 12 : Install Docker :




Run the following commands to install Docker:


```bash
sudo yum install docker
```

Start the docker service 

```bash
sudo service docker start
```
Add the ec2-user to the docker group to run Docker commands without 

```bash
sudo usermod -a -G docker ec2-user
```

## Step 13 : Restart Jenkins:

After adding the Jenkins user to the docker group, restart Jenkins to apply the changes.
