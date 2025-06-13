# HomePage

## build

构建前端项目

```bash
# 进入前端项目目录
cd src/main/web
# 如果是首次构建，先安装依赖，后续的构建可以跳过这一步
pnpm install
# 构建前端项目，构建产物会生成在 src/resources/static 目录下
pnpm build
```

构建后端项目

```
mvn clean install
```
