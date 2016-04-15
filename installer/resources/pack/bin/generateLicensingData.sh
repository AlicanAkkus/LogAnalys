#!/bin/sh

APP_HOME=/home/ferhat/workspace/TOFIntegratorV2/distsrc
LIB_PATH=$APP_HOME/lib
INTCLASSPATH=.

jars=$LIB_PATH/*.jar
for i in ${jars}
do
  if [ -z "$INTCLASSPATH" ] ; then
    INTCLASSPATH=$i
  else
    INTCLASSPATH="$i":$INTCLASSPATH
  fi
done

echo "INTCLASSPATH = $INTCLASSPATH"

java -cp $INTCLASSPATH com.j32bit.licenseManager.LicenseDataGenerator -g $LIB_PATH/TOFIntegratorV2.jar

exit 0
