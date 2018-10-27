echo ------------------------ Exercise 5 ------------------------
echo Introducing the JLINK tool
echo Make a minimal java image launching the insurance class
echo ----------------------------------- ------------------------

source setPath.sh
rm -rf img

set -e
set -x

jlink --list-plugins

./img/bin/insurance

du -sh img
