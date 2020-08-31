package Music_Advisor;

import Music_Advisor.api.*;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Menu {

    private static int currentPage = 0;
    private static ArrayList<String> toPrint;

    static void setCurrentPage(int currPage) {
        currentPage = currPage;
    }

    public static void launch() throws IOException, InterruptedException {
        Scanner in = new Scanner(System.in);
        String command = "";

        while(!command.equals("exit")) {
            command = in.nextLine();
            parseCommand(command);
        }

        System.out.println("---GOODBYE!---");
    }

    private static void parseCommand(String command) throws IOException, InterruptedException {
        if(command.equals("help")) {
            System.out.print("Available commands:\n" +
                    "\t- auth (authorize app)" +
                    "\t- featured (list of featured playlists)\n" +
                    "\t- new (list of new albums)\n" +
                    "\t- categories (list of available categories)\n" +
                    "\t- playlists <CATEGORY NAME> (list of playlists from chosen category)\n" +
                    "\t- exit\n");
        }

        if(command.equals("auth")) {
            Auth.init();

            if (Auth.isAuthorized()) {
                System.out.println("---SUCCESS---");
            } else {
                System.out.println("code not received");
                System.out.println("---FAILURE---");
            }
        }

        if(!Auth.isAuthorized()) {
            System.out.println("Please, provide access for application.");
            return;
        }

        if(command.equals("featured")) {
            JsonArray featured = Client.getFeatured();
            toPrint = Printer.printPlaylists(featured);
            currentPage = 0;
            Printer.print(toPrint, currentPage);
        }

        if(command.equals("new")) {
            JsonArray newReleases = Client.getNewReleases();
            toPrint = Printer.printAlbums(newReleases);
            currentPage = 0;
            Printer.print(toPrint, currentPage);
        }

        if(command.equals("categories")) {
            JsonArray categories = Client.getCategories();
            toPrint = Printer.printCategories(categories);
            currentPage = 0;
            Printer.print(toPrint, currentPage);
        }

        if(command.equals("prev")) {
            if(currentPage ==  0) {
                System.out.println("No more pages");
            } else {
                currentPage--;
                Printer.print(toPrint, currentPage);
            }
        }

        if(command.equals("next")) {
            if(currentPage == Printer.getTotalPages() - 1) {
                System.out.println("No more pages");
            } else {
                currentPage++;
                Printer.print(toPrint, currentPage);
            }
        }

        String[] playlists_command = command.split("\\s+");

        if(playlists_command[0].equals("playlists")) {
            String category = command.replace("playlists ", " ");
            JsonArray categoryPlaylists =  Client.getCategoryPlaylists(category);
            toPrint = Printer.printPlaylists(categoryPlaylists);
            currentPage = 0;
            Printer.print(toPrint, currentPage);
        }
    }
}