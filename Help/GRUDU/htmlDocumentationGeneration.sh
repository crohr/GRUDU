#!/bin/bash
mkdir -p UsersManual
rm -rf UsersManual/*
cd ../../doc/GRUDU_UserManual

latex2html  -split 4 -link 1 -white  -image_type "png" -local_icons -transparent -noaddress -noinfo Grudu-UM.tex -dir ../../Help/GRUDU/UsersManual -html_version  4.0 -no_footnode 

cd ../../Help/GRUDU/UsersManual
