void main() throws FileNotFoundException {
    var lines = new BufferedReader(new FileReader("./input.txt")).lines().toList();

    var resultA = solveA(lines);
    System.out.println(resultA);

    var resultB = solveB(lines);
    System.out.println(resultB);
}

String solveA(List<String> lines) {
    var result = lines.stream().map(s -> s.split(" ")).filter(this::isSafeLine).count();
    return Long.toString(result);
}

String solveB(List<String> lines) {
    var result = lines.stream().filter(this::anyPermutationIsSafe).count();
    return Long.toString(result);
}

boolean anyPermutationIsSafe(String line) {
    var numbers = line.split(" ");
    var skippedArray = new String[numbers.length - 1];

    for (int toSkip = 0; toSkip < numbers.length; toSkip++) {
        System.arraycopy(numbers, 0, skippedArray, 0, toSkip);
        System.arraycopy(numbers, toSkip + 1, skippedArray, toSkip, skippedArray.length - toSkip);
        if (isSafeLine(skippedArray)) {
            return true;
        }
    }
    return false;
}

boolean isSafeLine(String[] line) {
    var mode = 0;

    for (int i = 1; i < line.length; i++) {
        var previous = Integer.parseInt(line[i - 1]);
        var current = Integer.parseInt(line[i]);

        var diff = current - previous;

        if (diff == 0 || Math.abs(diff) > 3) {
            return false;
        }

        if (diff < 0 && mode > 0) {
            return false;
        }

        if (diff > 0 && mode < 0) {
            return false;
        }

        if (mode == 0) {
            mode = diff < 0 ? -1 : 1;
        }
    }

    return true;
}