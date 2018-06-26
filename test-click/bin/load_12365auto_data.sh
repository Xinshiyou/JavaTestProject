#appName=`cat ./appName.data`
appName=12365_auto
echo $appNmae
fileListUrl=http://116.62.12.8/$appName/data_file.list
echo $fileListUrl 
curl $fileListUrl > $appName.processing.file.list
if [ $? != 0 ];
then
   exit 0
fi

processedFileList=`cat ./$appName.processed.file.list`
processingFileList=`cat ./$appName.processing.file.list`

if [[ $processingFileList =~ "Not Found" ]]
then
    echo "$fileListUrl data was not found"
    rm -f $appName.processing.file.list
    exit 0
fi


cat $appName.processing.file.list | while read fileName
do
        if [[ $processedFileList =~ $fileName ]]
        then
            echo "file $fileName has been processed already"
            rm -f $appName.processing.file.list
            exit 0
        else
            echo "start to process file $fileName"
        fi

   echo $fileName
   fileUrl=http://116.62.12.8/$appName/$fileName
   echo "download file from: "$fileUrl
   curl $fileUrl > $appName.processingData.json
   if [ $? != 0 ];
   then
     echo "download failed:  $fileUrl"
   fi
   curl -XPUT 116.62.12.8:9200/test/T_12365AUTO_COMPLAINT/_bulk?pretty  --data-binary @$appName.processingData.json
   if [ $? == 0 ];
        then
             echo $fileName >> ./$appName.processed.file.list
        fi
done

rm -f $appName.processing.file.list