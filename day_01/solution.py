#! /bin/python

def solve_a(lines: list[str]) -> str:
    left_list: list[int] = []
    right_list: list[int] = []

    for line in lines:
        left_value, _, right_value = line.partition("   ")

        left_list.append(int(left_value))
        right_list.append(int(right_value))

    left_list.sort()
    right_list.sort()

    result = sum(abs(l - r) for l, r in zip(left_list, right_list))

    return str(result)


def solve_b(lines: list[str]) -> str:
    left_list: list[int] = []
    frequencies: dict[int, int] = {}

    for line in lines:
        left_value, _, right_value = line.partition("   ")
        left_list.append(int(left_value))

        right_number = int(right_value)
        frequencies[right_number] = frequencies.get(right_number, 0) + 1

    result = sum(l * frequencies.get(l, 0) for l in left_list)

    return str(result)


def solve() -> None:
    input: list[str] = read_input()

    result_a: str = solve_a(input)
    print(result_a)

    result_b: str = solve_b(input)
    print(result_b)


def read_input() -> list[str]:
    with open("./input.txt", "r") as input_file:
        return input_file.read().splitlines()

if __name__ == "__main__":
    solve()
