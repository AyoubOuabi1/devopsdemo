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

## Step 2: Add Jenkins Repository

Add the Jenkins repository using the following command:

```bash
[ec2-user ~]$ sudo wget -O /etc/yum.repos.d/jenkins.repo \ https://pkg.jenkins.io/redhat-stable/jenkins.repo
