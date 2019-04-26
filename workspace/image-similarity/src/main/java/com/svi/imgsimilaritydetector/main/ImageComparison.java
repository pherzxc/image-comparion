package com.svi.imgsimilaritydetector.main;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageComparison {

	public static void main(String[] args) {
		String folderPath1 = "C:/Users/jmata/Desktop/test folder/ANGEL JOHN INTEGRATED ACADEMY, INC";
		String folderPath2 = "C:/Users/jmata/Desktop/test folder/ANGEL JOHN INTEGRATED ACADEMY, INC - Copy";
		String image1 = null;
		String image2 = null;
		boolean result;
		
		File folderDir1 = new File(folderPath1);
		File folderDir2 = new File(folderPath2);
		
		String[] images1 = folderDir1.list();
		String[] images2 = folderDir2.list();
		
		for (int i = 0; i < images1.length; i++) {
			image1 = images1[i];
//			System.out.println(images1);
			for (int j = 0; j < images2.length; j++) {
//				if (i == j) {
//					continue;
//				}
				image2 = images2[j];
//				System.out.println(images2);
				
				result = isImagesSimilar(folderPath1 + "/" + image1, folderPath2 + "/" + image2);
//				System.out.println(result);
				
				if (result == true) {
					System.out.println(image1 + " , " + image2 + " are similar");
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

//			for (int i = 0; i < descriptors1.rows(); i++) {
//				System.out.println(match[i].distance);
//				double dist = match[i].distance;
//				if(dist < min_dist) {
//					min_dist = dist;
//				}
//				if(dist > max_dist) {
//					max_dist = dist;
//				}
//
//			}
//			System.out.println("max_dist= " + max_dist + ", min dist= " + min_dist);
			
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
