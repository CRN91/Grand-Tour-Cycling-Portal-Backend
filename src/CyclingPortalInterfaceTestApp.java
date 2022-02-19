package src;

import src.cycling.*;

import java.io.*;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the CyclingPortalInterface interface -- note you
 * will want to increase these checks, and run it on your CyclingPortal class
 * (not the BadCyclingPortal class).
 *
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class CyclingPortalInterfaceTestApp {

  public void whenSerializingCyclingPortal_FileIsSaved(CyclingPortal cyclingPortal,
                                                       String fileName) {
    try {
      cyclingPortal.saveCyclingPortal(fileName);
    } catch (IOException ex) {
      System.out.println("File failed to write!");
    }

    File f = new File(fileName);
    assert(f.exists() && !f.isDirectory()) : "File failed to save!";
  }

  /**
   * Test method.
   * @param args not used
   */
  public static void main(String[] args) {
    System.out.println("The system compiled and started the execution...");
    CyclingPortalInterfaceTestApp testApp = new CyclingPortalInterfaceTestApp();

    CyclingPortal cyclingPortal = new CyclingPortal();
    testApp.whenSerializingCyclingPortal_FileIsSaved(cyclingPortal, "Test.txt");
  }
}
