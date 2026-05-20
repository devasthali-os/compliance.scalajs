FROM centos

RUN yum -y install curl
RUN yum -y install epel-release
RUN curl --silent --location https://rpm.nodesource.com/setup_11.x | bash -
RUN yum -y install nodejs
RUN yum -y install gcc-c++ make
RUN yum -y install git

RUN echo -e "verifying nodejs installation"
RUN npm -v

RUN yum install -y \
       java-1.8.0-openjdk \
       java-1.8.0-openjdk-devel

ENV JAVA_HOME /etc/alternatives/jre

RUN npm pack https://github.com/duwamish-os/introv.scalajs.git
RUN tar xf *.tgz
RUN mv package/* .
RUN rmdir package
RUN npm install
RUN node node_modules/sjs-nodejs/index.js
RUN node_modules/sbt/bin/sbt buildJs

EXPOSE 8080

CMD ["node","./server.js"]