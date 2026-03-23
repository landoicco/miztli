# Miztli

This is a basic learning-focused application designed to store essential information about pets, such as dogs and cats.

It allows users to save and manage pet data—like name, age, breed, and medical details—into a DynamoDB table on AWS. Built primarily for educational purposes.

What about the name... Miztli? [Find out!](https://gdn.iib.unam.mx/diccionario/miztli/11008)

## How to deploy

First, build the project. Aka, put the "chalan" to work!

``` shell
chmod +x chalan.sh
./chalan.sh
```

`chalan.sh` is a helper script to format and build the code of both of the required projects to run and deploy the app.

Once is built, which means, the file `miztli-lambda.jar` is on the project level, make a deploy of the code and the infrastructure on AWS. To be safe, you can do a `cdk diff` to see everything is good. Finally:

``` shell
cdk deploy
```

Then, if you make changes in code and want to deploy quickly to AWS, run the combo:
``` shell
./chalan.sh
cdk deploy --hotswap
```

