FROM java:latest

ENV APPDIR /app

ADD ./ $APPDIR
RUN cd $APPDIR && ./activator compile

EXPOSE 9000

WORKDIR $APPDIR
ENTRYPOINT ["./activator"]
CMD ["-Dapplication.settingsWidget.enabled=false", "run"]
