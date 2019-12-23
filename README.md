## [AndroidPictureUpload]
# AndroidPictureUpload
Android使用okhttp3将图片上传到php服务器，进度更新，图片裁剪 
* QQ群：__150653052__ <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=060e1e3319d90c1ece6c02396a91ac8625bedbbf138f9a66a3552bdfe8a51d11"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="江苏网络监控安装运维" title="江苏网络监控安装运维"></a>

# php
一个php文件，放在服务端即可，将程序源码中的路径指向此文件

 ```php
        <?php

            $field = 'file';
            //对应webupload里设置的fileVal

            $savePath = './uploads/';    // 这里注意设置uploads文件夹的权限
            if(!is_dir($savePath)){
                mkdir($savePath,0777,true);
            }

            $saveName = time() . uniqid() . '_' . $_FILES[$field]['name']; // 为文件重命名
            $fullName = $savePath . $saveName;

            if (file_exists($fullName)) {
              $result = [
                  'success'=>false,
                  'message'=>'相同文件名的文件已经存在'
                ];
            }else{
                move_uploaded_file($_FILES[$field]["tmp_name"], $fullName);
                $result = ['success'=>true, 'fullName'=>$fullName];
            }

            return json_encode($result); // 将结果打包成json格式返回

        ?>

 ```