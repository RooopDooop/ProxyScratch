FROM openjdk
WORKDIR /ProxyFetcher/Software
COPY app/build/libs .
ENTRYPOINT java -jar app.jar

#docker build -t watchmejump/proxyfetcher:0.0.1 .
#docker run -d --net krakenNetwork --restart=always --ip 172.1.1.21 -p 8080:8080 --name pairAssetInstance watchmejump/proxyfetcher:0.0.1