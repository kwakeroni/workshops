echo ------------------------ Exercise 5 ------------------------
echo Introducing the JLINK tool
echo Make a minimal java image launching the insurance class
echo ----------------------------------- ------------------------

source setPath.sh
rm -rf img

set -e
set -x

jlink --module-path $J9HOME/jmods:mods --add-modules java.base,workshop.insurance,workshop.my.policy.provider --output img --launcher insurance=workshop.insurance --compress=2 --strip-debug


#jlink --list-plugins |grep Option

#jlink --module-path $J9HOME/jmods:mods --add-modules java.base,workshop.insurance,workshop.my.policy.provider --output img --launcher insurance=workshop.insurance --compress=2
#jlink --module-path $J9HOME/jmods:mods --add-modules java.base,workshop.insurance,workshop.my.policy.provider --output img --launcher insurance=workshop.insurance $@


./img/bin/insurance

du -sh img
