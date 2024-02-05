# Downloading and Installing Jenkins and SonarQube on AWS EC2

This guide provides step-by-step instructions for downloading and installing Jenkins and SonarQube on an Amazon EC2 instance. Before you begin, ensure you have an EC2 instance running Amazon Linux and connect to the instance.

## Prerequisites

- An Amazon EC2 instance with Amazon Linux(for Jenkins).
- An Amazon EC2 instance with Ubunto(for SonarQube).
- Access to the EC2 instance through SSH.


## Start Intalling Jenkins

## Introduction

Completing the following steps will enable you to download and install Jenkins on your AWS EC2 instance. Follow the steps below after connecting to your EC2 instance.

## Step 1: Update Software Packages

Ensure that your software packages are up to date on your instance by using the following command to perform a quick software update:

```bash
sudo yum update –y
```
## Step 2: Add Jenkins Repository

Add the Jenkins repository using the following command:

```bash
 sudo wget -O /etc/yum.repos.d/jenkins.repo \ https://pkg.jenkins.io/redhat-stable/jenkins.repo
```

## Step 3: Import Jenkins-CI Key

Import a key file from Jenkins-CI to enable installation from the package:

```bash
 sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io-2023.key
```
```bash
 sudo yum upgrade
```

## Step 4: Install Java

Install Java :

```bash
 sudo dnf install java-17-amazon-corretto -y
```
## Step 5: Install Jenkins

Install Jenkins:

```bash
 sudo yum install jenkins -y
```

## Step 6: Enable Jenkins Service

Enable the Jenkins service to start at boot:

```bash
 sudo systemctl enable jenkins
```

## Step 7: Start Jenkins Service

Start Jenkins as a service:

```bash
sudo systemctl start jenkins
```
## Step 8: Check Jenkins Service Status

You can check the status of the Jenkins service using the command: 

```bash
 sudo systemctl status jenkins
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


## Introduction
In this section, we will guide you through the process of downloading and installing SonarQube on your Ubuntu EC2 instance.

## Step 1: Update Software Packages

Ensure that your software packages are up to date on your Ubuntu instance by using the following command to perform a quick software update:

```bash
 sudo apt update && sudo apt upgrade -y
```

## Step 2: Install OpenJDK

SonarQube requires Java to run. Install OpenJDK on your Ubuntu instance:

```bash
sudo apt install openjdk-17-jdk -y

```
## Step 3: Download and install SonarQube

Download and install SonarQube by running the following commands:

```bash
sudo wget https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-9.8.0.63668.zip
```
```bash
sudo apt-get install zip -y
```
```bash
sudo mv sonarqube-9.8.0.63668 /opt/sonarqube
```

## Step 4: Configure SonarQube

 Create a group name sonar
```bash
 sudo groupadd sonar
```

 Give ownership permission to the sonar user and group.
```bash
sudo useradd -d /opt/sonarqube -g sonar sonar

```

```bash
sudo chown sonar:sonar /opt/sonarqube -R
```



This file is now required for SonarQube to run as a service.
```bash
sudo nano /etc/systemd/system/sonar.service
```

Add the following to the file.


```bash
[Unit]

Description=SonarQube service

After=syslog.target network.target


[Service]

Type=forking


ExecStart=/opt/sonarqube/bin/linux-x86-64/sonar.sh start

ExecStop=/opt/sonarqube/bin/linux-x86-64/sonar.sh stop


User=sonar

Group=sonar

Restart=always


LimitNOFILE=65536

LimitNPROC=4096


[Install]

WantedBy=multi-user.target
```

The next step is to enable and start SonarQube as a service.

```bash
sudo systemctl enable sonar
```

```bash
sudo systemctl start sonar
```

```bash
sudo systemctl status sonar
```

