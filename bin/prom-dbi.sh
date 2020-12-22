APP_NAME="prom-dbi-service"
APP_VERSION="0.0.1-SNAPSHOT"
JAVA_PARAM="-Xmx351m"

BIN_PATH=$PROM_HOME_PARENT/MORP/$APP_NAME/bin     #PROM-HOME-PARENT :: exported in .bashrc
JAR_PATH=$BIN_PATH/../target/$APP_NAME-$APP_VERSION.jar

echo "Starting '$APP_NAME' with java param: '$JAVA_PARAM', at '$JAR_PATH'"
java $JAVA_PARAM -jar $JAR_PATH
