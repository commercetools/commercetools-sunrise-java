FROM java:latest

ENV APPDIR /app

ADD ./ $APPDIR
RUN cd $APPDIR && ./activator stage

EXPOSE 9000

WORKDIR $APPDIR
ENTRYPOINT ["/app/target/universal/stage/bin/sphere-sunrise"]
CMD ["-Dconfig.resource=prod.conf"]
