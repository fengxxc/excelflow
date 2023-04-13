```json
{
    "config": {
        "validateStrategy": null // (gridValue) => {validate: Boolean, message: String}
    },
    // pipelines
    "pipelines": [{
        "id": "",
        "object": null, // will reflect to this object type
        "sheet": null, // sheetName or sheetAt, null is all sheet
        "cellMap": [
            {
                "cellPoint": {
                    "x": 0,
                    "y": 0
                },
                "cellRef": "", // if "point" null, this effective
                "as": "", // data propty alias
                "type": "", // 'text', 'number', 'date', 'currency‘, 'percent', ...
                "format": "", //'number', 'date', ..., such as "YYYY-MM-DD"
            },
        ],
        "iterative": false, // true or false
        "foward": "down", // active when "iterative" is true. 'up', 'down', 'left', 'right'. default: 'down'
        // "stepLength": 1, // active when "iterative" is true. number of steps
        // "stepTotal": -1 // active when "iterative" is true. -1 means infinite
    },]
}
```