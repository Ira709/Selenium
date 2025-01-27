package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/Users/trojan4ik/Downloads/chromedriver-mac-arm64");

        WebDriver driver = new ChromeDriver();

        driver.get("https://demoqa.com/books");


        List<WebElement> tableRowsIncludingHeader = driver.findElements(By.className("rt-tr"));
        List<WebElement> filteredTableRows = tableRowsIncludingHeader.stream()
                .filter(row -> !row.getAttribute("class").contains("-padRow"))
                .collect(Collectors.toList());


        final String infoDelimiter = " - ";

        String headerValues = filteredTableRows.get(0)
                .findElements(By.className("rt-resizable-header-content"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.joining(infoDelimiter));

        System.out.println(headerValues);


        filteredTableRows.stream().skip(1)
                .map(tableRow -> {
                    String imgSrc = tableRow.findElement(By.tagName("img")).getAttribute("src");

                    WebElement bookCell = tableRow.findElement(By.tagName("a"));
                    String bookLink = bookCell.getAttribute("href");
                    String bookTitle = bookCell.getText();

                    final int authorCellIndex = 2;
                    String bookAuthor = tableRow.findElements(By.className("rt-td")).get(authorCellIndex).getText();

                    final int publisherCellIndex = 3;

                    String publisherCellXpath = String.format("(.//div)[%d]", publisherCellIndex + 1);
                    String bookPublisher = tableRow.findElement(By.xpath(publisherCellXpath)).getText();

                    String tableRowInfo = String.join(infoDelimiter, imgSrc, "(" + bookLink, bookTitle + ")", bookAuthor, bookPublisher);
                    return tableRowInfo;
                })
                .forEach(System.out::println);

        driver.quit();
    }
}


