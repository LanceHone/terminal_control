export class StringUtils {
    /**
     * 检查字符串是否为空或 undefined
     * @param {string} str
     * @returns {boolean}
     */
    static isEmpty(str) {
        return str === null || str === undefined || str.trim() === '';
    }

    /**
     * 检查字符串是否不为空
     * @param {string} str
     * @returns {boolean}
     */
    static isNotEmpty(str) {
        return !this.isEmpty(str);
    }

    /**
     * 检查字符串是否为空或只包含空格
     * @param {string} str
     * @returns {boolean}
     */
    static isBlank(str) {
        return str === null || str === undefined || str.trim().length === 0;
    }

    /**
     * 检查字符串是否不为空或只包含空格
     * @param {string} str
     * @returns {boolean}
     */
    static isNotBlank(str) {
        return !this.isBlank(str);
    }

    /**
     * 检查字符串是否只包含数字
     * @param {string} str
     * @returns {boolean}
     */
    static isNumeric(str) {
        return /^-?\d+(\.\d+)?$/.test(str);
    }

    /**
     * 将字符串转为小写
     * @param {string} str
     * @returns {string}
     */
    static toLowerCase(str) {
        return str.toString().toLowerCase();
    }

    /**
     * 将字符串转为大写
     * @param {string} str
     * @returns {string}
     */
    static toUpperCase(str) {
        return str.toString().toUpperCase();
    }

    /**
     * 检查字符串是否包含指定子字符串
     * @param {string} str
     * @param {string} substring
     * @returns {boolean}
     */
    static contains(str, substring) {
        return str.includes(substring);
    }

    /**
     * 检查字符串是否包含指定子字符串（不区分大小写）
     * @param {string} str
     * @param {string} substring
     * @returns {boolean}
     */
    static containsIgnoreCase(str, substring) {
        return this.toLowerCase(str).includes(this.toLowerCase(substring));
    }

    /**
     * 将字符串按指定分隔符切分，并去除空字符串和空白项
     * @param {string} str - 要切分的字符串
     * @param {string} [delimiter='/\\'] - 分隔符（默认为 / 或 \）
     * @returns {string[]} 切分结果数组
     */
    static splitAndTrim(str, delimiter = '/\\') {
        const splitPattern = new RegExp(`[${delimiter.replace(/[-/\\^$*+?.()|[\]{}]/g, '\\$&')}]`);
        return str.split(splitPattern).filter(part => part.trim() !== '');
    }
}

// 示例用法
// console.log(StringUtils.isEmpty('')); // true
// console.log(StringUtils.isNotEmpty('hello')); // true
// console.log(StringUtils.isNumeric('123')); // true
// console.log(StringUtils.contains('Hello world', 'world')); // true
// console.log(StringUtils.containsIgnoreCase('Hello World', 'WORLD')); // true
//
// console.log(StringUtils.splitAndTrim('//example/test\\new//folder\\'));
// // 输出: ["example", "test", "new", "folder"]
//
// console.log(StringUtils.splitAndTrim('a;;b;c;', ';'));
// // 输出: ["a", "b", "c"]
