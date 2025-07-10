
/**
 * 生成随机字符串
 * @param length 字符串长度
 * @returns 随机字符串
 */
export const randomString = (length: number = 6): string => {
    const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    let result = "";
    for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
}

/**
 * Pre-process form data before sending to server.
 * @param formData any
 */
export const preProcFormData = (formData: any) => {
    for (let key in formData) {
        if (formData[key] == null || formData[key] == undefined) {
            delete formData[key];
        }
        if (formData.hasOwnProperty(key)) {
            if (typeof formData[key] === "string") {
                formData[key] = formData[key].trim();
            }
        }
        if (Array.isArray(formData[key])) {
            for (let i = 0; i < formData[key].length; i++) {
                if (typeof formData[key][i] === "string") {
                    formData[key][i] = formData[key][i].trim();
                }
            }
        }
        if (typeof formData[key] === "object") {
            formData[key] = preProcFormData(formData[key]);
        }
    }
}