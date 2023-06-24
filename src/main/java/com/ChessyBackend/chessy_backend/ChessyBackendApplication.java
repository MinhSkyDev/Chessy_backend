package com.ChessyBackend.chessy_backend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@SpringBootApplication
public class ChessyBackendApplication {



	public static void main(String[] args) throws IOException {
//			ClassLoader classLoader = ChessyBackendApplication.class.getClassLoader();
//			File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());


		InputStream serviceAccount =
				new ClassPathResource("serviceAccountKey.json").getInputStream();

//			FileInputStream serviceAccount =
//					new FileInputStream(FirebaseService.getFile().getAbsolutePath());
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();

			FirebaseApp.initializeApp(options);

			SpringApplication.run(ChessyBackendApplication.class, args);
		}
}
