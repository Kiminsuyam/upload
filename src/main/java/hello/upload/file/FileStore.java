package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

	@Value("${file.dir}")
	private String fileDir;

	public String getFullPath(String filename) {
		return fileDir + filename;
	}

	//이미지를 여러개 업로드
	public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
		List<UploadFile> storeFileResult = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			if (!multipartFile.isEmpty()) {
				storeFileResult.add(storeFile(multipartFile));
			}
		}
		return storeFileResult;
	}

	//하나를 업로드
	public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
		if(multipartFile.isEmpty()) {
			return null;
		}

		String originalFilename = multipartFile.getOriginalFilename();
		//서버에 저장하는 파일명
		String storeFileName = createStoreFileName(originalFilename);
		multipartFile.transferTo(new File(getFullPath(storeFileName)));
		return new UploadFile(originalFilename, storeFileName);
	}

	private String createStoreFileName(String originalFilename) {

		String ext = extractExt(originalFilename);

		//서버에 저장하는 파일명
		String uuid = UUID.randomUUID().toString();
//		"qwe-qwe-123-qwe-qqwe";

		return uuid + "." + ext;
//		"qwe-qwe-123-qwe-qqwe.png";
	}

	private String extractExt(String originalFilename) {
		int pos = originalFilename.lastIndexOf("."); //위치 가져오기
		return originalFilename.substring(pos + 1);
	}

}
