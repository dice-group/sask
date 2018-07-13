## Deployment
==========================

Ansible is used as a deployment and configuration tool for deploying SASK microservices using docker.
Introduction to ansible : https://sysadmincasts.com/episodes/43-19-minutes-with-ansible-part-1-4
                          https://serversforhackers.com/c/an-ansible-tutorial


Initial setup of your host system when ansible would be installed:

1. Exchange your ssh public keys with the VM/instances where you want to deploy the microservices.(This is an one setup, but if you prefer to use password for each time login one could use that approach aswell)
   Ex : ssh-copy-id -i $HOME/.ssh/mykey.pub user@host

2. Install ansible version 2.4.0.0:
     a) linux machines : You can use either pip,apt-get,yum to install ansible. Ex: pip install ansible==2.4.0.0
	 b) Mac :  Use brew to install pip, later use pip to install ansible
	 c) Windows:  http://www.oznetnerd.com/installing-ansible-windows/

Deploying the microservices:

Move to the playbooks directory of the sask project

1. Building docker images.(Currently an ansible playbook builds and pushes the image, this step would be replaced with automatic docker image build with docker cloud)
	
   ansible-playbook -i inventory/dev.inventory build-images.yml --become --ask-become-pass
   This pushes all the docker images with the default docker tag "latest"

   ansible-playbook -i inventory/dev.inventory build-images.yml --become --ask-become-pass --extra-vars "image_tag=your choice of tagname"
   The docker images with your choice of tag name is built

2. Deploying the microservices, by pulling the docker images from docker hub

   ansible-playbook -i inventory/sask_dev.inventory deploy_microservices.yml
   
   If you want to deploy the images with the custom tag which you provided in step 1:
   
   ansible-playbook -i inventory/sask_dev.inventory --extra-vars "image_tag=your choice of tagname" deploy_microservices.yml

3. Once deployed and tested, if you want to tear down your all the microservices

   ansible-playbook -i inventory/sask_dev.inventory --extra-vars "container_state=absent" deploy_microservices.yml 
	  
	 
   

