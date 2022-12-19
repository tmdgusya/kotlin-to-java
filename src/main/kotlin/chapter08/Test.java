package chapter08;

import java.util.Comparator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Shortlists.sorted(List.of("123123", "!23123", "!@31231"), Comparator.comparingInt(String::length));
    }
}
