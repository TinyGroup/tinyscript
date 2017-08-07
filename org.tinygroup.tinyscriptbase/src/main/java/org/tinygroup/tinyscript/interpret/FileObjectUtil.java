package org.tinygroup.tinyscript.interpret;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

public final class FileObjectUtil {

	private FileObjectUtil(){
		
	}
	
	public static FileObject findFileObject(String path,boolean findInJar) throws Exception{
		//优先根据路径匹配
		FileObject fileObject = VFS.resolveFile(path);
		if(fileObject!=null && fileObject.isExist()){
		   return fileObject;
		}
		//遍历资源扫描器
		FileResolver fileResolver = BeanContainerFactory
                .getBeanContainer(
                		FileObjectUtil.class.getClassLoader())
                .getBean(FileResolver.BEAN_NAME);
		if(fileResolver!=null){
		   for(String sPath:fileResolver.getScanningPaths()){
			   FileObject dir = VFS.resolveFile(sPath);
			   if(!dir.isInPackage() || findInJar){
				   FileObject result = findFileObject(dir,path);
				   if(result!=null){
					  return result;
				   }
			   }
		   }
		}
		return null;
	}
	
	private static FileObject findFileObject(FileObject fileObject,String path){
		
		if(fileObject.getPath().equals(path)){
		   return fileObject;
		}
		if(fileObject.getChildren()!=null){
		   for(FileObject child:fileObject.getChildren()){
			   FileObject result = findFileObject(child,path);
			   if(result!=null){
				  return result; 
			   }
		   }
		}
		return null;
	}
}
