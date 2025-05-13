#!/bin/bash

# sudo systemctl stop sshd
kill -9 $(ps aux | grep pypy | grep -v grep | awk '{print $2}')
sudo systemctl stop ruoyi.service
sudo systemctl stop docker