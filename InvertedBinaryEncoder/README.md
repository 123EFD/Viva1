1)Why using getBytes instead of working directl with Java Chars?

-->getBytes(StandardCharsets.UTF_8): Converts the Java String into an array of bytes using the UTF‑8 encoding.
-->Based on the encoding rule, it only operates on 8‑bit binary values. To invert exactly 8 bits,user must operate on bytes, not 16‑bit Java chars.

2)Why use StandardCharsets.UTF_8 (explicit Charset) instead of getBytes() with no args?

-->This will use the platform's default charset .StandardCharsets.UTF_8 is safe (no checked exception) and efficient — it’s a constant provided by the JDK.

-->E.g.: ASCII letter 'A' -> UTF-8 bytes [0x41] -> invert 0x41 -> invert low 8 bits -> result.
More info.:[text](https://dev.to/satyam_gupta_0d1ff2152dcc/java-string-getbytes-explained-your-ultimate-guide-to-character-encoding-4emn)

