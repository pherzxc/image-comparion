package com.svi.imgsimilaritydetector.main;

import java.io.File;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageComparison1 {

	public static void main(String[] args) {
		
		String folderPath1 = null;
		String folderPath2 = null;
		String image1 = null;
		String image2 = null;
		boolean result;
		
		String mainFolderPath = "C:/Users/jmata/Desktop/test folder";
		
		File mainFolder = new File(mainFolderPath);
		String[] folders = mainFolder.list();
		
		for (int i = 2; i < folders.length; i++) {
			folderPath1 = mainFolder + "/" + folders[i];
			System.out.println("Folder1: " + folderPath1);
			
			for (int j = 3; j < folders.length; j++) {
				if (i != j) {
					folderPath2 = mainFolder + "/" + folders[j];
					System.out.println("Folder2: " + folderPath2);
					
					File folderDir1 = new File(folderPath1);
					File folderDir2 = new File(folderPath2);
					
					String[] images1 = folderDir1.list();
					String[] images2 = folderDir2.list();
					
					System.out.println(images2[1].toString());
					
					for (int x = 0; x < images1.length; x++) {
						image1 = images1[x];
						
						for (int y = 0; y < images2.length; y++) {

							image2 = images2[y];
					
							result = isImagesSimilar(folderPath1 + "/" + image1, folderPath2 + "/" + image2);
							System.out.println("1: " + image1 + "    " + "2: " + image2);
							
							if (result == true) {
								System.out.println(image1 + " , " + image2 + " are similar");
							}
						}
					}
					
				}
			}
		}
	}

	public static boolean isImagesSimilar(String image1, String image2) {
		boolean value = false;
		int accurateFeatCount = 0;
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
	    DescriptorExtractor extractor  = DescriptorExtractor.create(DescriptorExtractor.ORB);
	    DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
	    
	    Mat img1 = Imgcodecs.imread(image1);
	    Mat descriptors1 = new Mat();
	    MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
	    
		Mat img2 = Imgcodecs.imread(image2);
		Mat descriptors2 = new Mat();
	    MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
	    
	    detector.detect(img1, keypoints1);
	    detector.detect(img2, keypoints2);
	    
	    extractor.compute(img1, keypoints1, descriptors1);
	    extractor.compute(img2, keypoints2, descriptors2);
	    
	    MatOfDMatch matches = new MatOfDMatch();
		
	    
		if(descriptors2.cols() == descriptors1.cols()) {
			matcher.match(descriptors1, descriptors2, matches);

			DMatch[] match = matches.toArray();

			double max_dist = 0;
			double min_dist = 100;

			for (int i = 0; i < descriptors1.rows(); i++) {
				System.out.println(match[i].distance);
				double dist = match[i].distance;
				if(dist < min_dist) {
					min_dist = dist;
				}
				if(dist > max_dist) {
					max_dist = dist;
				}

			}
			System.out.println("max_dist= " + max_dist + ", min dist= " + min_dist);
			
			double thresholdDistance = 100 * 0.10;
			
			for (int i = 0; i < match.length; i++) {
				if (match[i].distance <= thresholdDistance) {
					accurateFeatCount++;
				}
			}

			
			if (accurateFeatCount > match.length * 0.90) {
				value = true;
			} else {
				value = false;
			}
			
		} 
		return value;
	}


}
