package com.eltonquek.showbooking;

import com.eltonquek.showbooking.commands.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class ShowBookingApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ShowBookingApplication.class, args);
    }

    private final List<Command> commandList;

    @Override
    public void run(String... args) {
        if (isRunningInTest(args)) return;

        // Initialization
        Scanner scanner = new Scanner(System.in);
        introduction();

        // Read user input
        try {
            while (true) {
                System.out.print("enter command: ");
                String line = scanner.nextLine().trim();
                processInput(line);
            }
        } catch (Exception e) {
            log.error("Input error", e);
        }

        scanner.close();
        System.exit(0);
    }

    private static boolean isRunningInTest(String[] args) {
        return args != null && args.length == 1 && Objects.equals(args[0], "test");
    }

    private static void introduction() {
        System.out.println("Welcome to Elton's Show Booking Console App.");
        System.out.println("To start, please setup a new show with the command \"Setup <Show Number> <Number of Rows> <Number of seats per row> <Cancellation window in minutes>\".");
    }

    private void processInput(String line) {
        // Validate input
        String[] inputs = line.split(" ");
        Optional<Command> optionalCommand = commandList.stream()
                .filter(currentCommand -> currentCommand.validate(inputs))
                .findFirst();

        // Run command if input is valid
        if (optionalCommand.isPresent()) {
            optionalCommand.get().run(inputs);
        } else {
            System.out.println("Command entered was invalid, please try again.");
        }
    }
}
