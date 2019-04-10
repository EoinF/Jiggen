
export const saveState = (key: string, values: any) => {
    try {
        localStorage.setItem(key, JSON.stringify(values));
    } catch(err) {
        console.log(err);
    }
}

export const loadState = (key: string) => {
    try {
        const state = localStorage.getItem(key);

        if (state != null) {
            return JSON.parse(state);
        }
    } catch(err) {
        console.log(err);
    }
    return null;
}