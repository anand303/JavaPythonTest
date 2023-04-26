package SpringReactiveWeb.Sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.web.client.RestTemplate;


public class ReactiveFileUploadClient {
    
    //private WebClient webClient; 
    
	public String upload() {
			
        final WebClient webClient = WebClient.create();
        byte[] filearray = null;
		try {
			filearray = Files.readAllBytes(Paths.get("Hello.pdf"));
			//System.out.println(filearray);     
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String fileContent = new String(filearray);
        //System.out.println(fileContent);     
        String resp2 = "";
		Mono<String> resp1 = webClient.post()
		        .uri("http://localhost:7071/api/postfile")
		        .contentType(MediaType.MULTIPART_FORM_DATA)		   
		        .bodyValue(filearray)
		        //.body(BodyInserters.fromMultipartData("file",filearray))
		        //.body(BodyInserters.fromMultipartData("file",Files.readAllBytes(Paths.get("Hello.txt"))))
		        //.body(BodyInserters.fromMultipartData(fromFile(new File("Hello.txt"))))
		        .exchangeToMono(resp -> {
		        	if(resp.statusCode().is5xxServerError()) {
		        		resp.body((clientHttpResponse, context)->{
		        			return clientHttpResponse.getBody();
		        		});
		        	}
		        	return resp.bodyToMono(String.class); 
		        });
		
		return resp1.block();
    }

    public MultiValueMap<String, HttpEntity<?>> fromFile(File file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        //builder.part("file", new FileSystemResource(file));
        String header = String.format("form-data; name=%s; filename=%s", "paramName", "fileName.txt");

        try {
			builder.part("file", Files.readAllBytes(Paths.get("Hello.txt")))
			.headers(h -> {
			    h.setContentDispositionFormData("file", file.getName());
			    h.setContentType(MediaType.MULTIPART_FORM_DATA);
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        return builder.build();
    }   

public MultiValueMap<String, HttpEntity<?>> fromFile1(FileStore file) {
    MultipartBodyBuilder builder = new MultipartBodyBuilder();
    //builder.part("file", new FileSystemResource(file));
    String header = String.format("form-data; name=%s; filename=%s", "paramName", "fileName.txt");

    //builder.part("file", new ByteArrayResource(Files.readAllBytes(file.toPath()))).filename("Hello");
	builder.part("file", file);
    return builder.build();
}   
}