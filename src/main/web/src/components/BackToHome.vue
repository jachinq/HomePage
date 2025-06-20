<template>

    <div class="cursor-pointer group flex flex-row items-center" @click="$router.push('/')">
        <span class="text-sm text-blue-200 group-hover:text-blue-400">Home</span>
        <div class="icon-wrapper w-4 h-4 overflow-hidden">
            <svg t="1750431851413" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                p-id="14626" width="200" height="200">
                <path
                    d="M628.352 265.92h-176.64V128L64 314.24l387.712 186.304v-137.6h191.296c151.424 0 227.2 60.928 227.2 182.848 0 126.144-78.272 189.184-234.752 189.184H189.248V832H640c213.312 0 320-92.352 320-276.992 0-192.704-110.528-289.088-331.648-289.088z"
                    fillp-id="14627" class=" fill-blue-200 group-hover:fill-blue-400"></path>
            </svg>
        </div>
    </div>
</template>

<style>
.icon-wrapper svg {
    width: 100%;
    height: 100%;
    object-fit: contain;
}
</style>