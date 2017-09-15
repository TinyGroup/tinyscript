package org.tinygroup.tinyscript.interpret;

import java.io.File;
import java.io.IOException;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.vfs.impl.FileObjectImpl;
import org.tinygroup.vfs.impl.FileSchemaProvider;

public final class FileObjectUtil {

	private FileObjectUtil() {

	}

	public static FileObject findFileObject(String path, boolean findInJar) throws Exception {
		// 优先根据路径匹配
		FileObject fileObject = VFS.resolveFile(path);
		if (fileObject != null && fileObject.isExist()) {
			return fileObject;
		}
		// 遍历资源扫描器
		FileResolver fileResolver = BeanContainerFactory.getBeanContainer(FileObjectUtil.class.getClassLoader())
				.getBean(FileResolver.BEAN_NAME);
		if (fileResolver != null) {
			for (String sPath : fileResolver.getScanningPaths()) {
				FileObject dir = VFS.resolveFile(sPath);
				if (!dir.isInPackage() || findInJar) {
					FileObject result = findFileObject(dir, path);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	public static FileObject getOrCreateFile(String fileUrl, boolean findInJar) throws ScriptException, IOException {

		String parentFile = fileUrl.substring(0, fileUrl.lastIndexOf("/"));
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

		FileObject fileObject = VFS.resolveFile(parentFile);
		if (fileObject != null && fileObject.isExist()) {
			return getOrCreateChildFile(fileObject, fileName);
		}

		// 遍历资源扫描器
		FileResolver fileResolver = BeanContainerFactory.getBeanContainer(FileObjectUtil.class.getClassLoader())
				.getBean(FileResolver.BEAN_NAME);
		if (fileResolver != null) {
			for (String sPath : fileResolver.getScanningPaths()) {
				FileObject dir = VFS.resolveFile(sPath);
				if (!dir.isInPackage() || findInJar) {
					FileObject result = getOrCreateChildFile(findFileObject(dir, parentFile), fileName);
					if (result != null && result.isExist()) {
						return result;
					}
				}
			}
		}
		return null;

	}

	private static FileObject findFileObject(FileObject fileObject, String path) {

		if (fileObject.getPath().equals(path)) {
			return fileObject;
		}
		if (fileObject.getChildren() != null) {
			for (FileObject child : fileObject.getChildren()) {
				FileObject result = findFileObject(child, path);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	private static FileObject getOrCreateChildFile(FileObject parent, String fileName) throws IOException {

		if (parent == null) {
			return null;
		}
		
		if (parent.getChild(fileName) == null) {
			File file = new File(parent.getAbsolutePath() + File.separator + fileName);
			if (file.createNewFile()) {
				return new FileObjectImpl(new FileSchemaProvider(), file);
			}
		} else {
			return parent.getChild(fileName);
		}

		return null;
	}
}
