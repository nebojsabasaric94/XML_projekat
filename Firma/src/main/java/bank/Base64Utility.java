package bank;

import java.io.IOException;
import java.util.Base64;


public class Base64Utility {
	
		// Pomocna funkcija za enkodovanje bajtova u string
		public static String encode(byte[] data) {
			//Base64 encoder = new org.bouncycastle.util.encoders.Base64();
			//return encoder.encode(data);
			return new String(Base64.getEncoder().encode(data));
		}

		// Pomocna funkcija za dekodovanje stringa u bajt niz
		public static byte[] decode(String base64Data) throws IOException {
			/*BASE64Decoder decoder = new BASE64Decoder();
			return decoder.decodeBuffer(base64Data);
			*/
			return Base64.getDecoder().decode(base64Data.getBytes());
		}
	
}
