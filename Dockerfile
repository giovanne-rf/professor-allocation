FROM maven:3.6.3-jdk-8

ADD ./ /professor-allocation/

WORKDIR /professor-allocation/

RUN chmod -R +x /professor-allocation/